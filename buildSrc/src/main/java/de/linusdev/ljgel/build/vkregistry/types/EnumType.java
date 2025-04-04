/*
 * Copyright (c) 2024-2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.ljgel.build.vkregistry.types;

import de.linusdev.ljgel.build.vkregistry.RegistryLoader;
import de.linusdev.ljgel.build.vkregistry.types.abstracts.Type;
import de.linusdev.ljgel.build.vkregistry.types.abstracts.TypeType;
import de.linusdev.llog.LLog;
import de.linusdev.llog.base.LogInstance;
import de.linusdev.lutils.codegen.SourceGenerator;
import de.linusdev.lutils.codegen.java.*;
import de.linusdev.lutils.nat.enums.NativeEnumMember32;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.linusdev.ljgel.build.vkregistry.RegistryLoader.VULKAN_PACKAGE;

public class EnumType implements Type {

    public static final LogInstance LOG = LLog.getLogInstance();

    private final static @NotNull String SUB_PACKAGE = VULKAN_PACKAGE + ".enums";

    private final @NotNull String name;
    private final @Nullable String comment;
    private final @NotNull String namePrefixToIgnore;
    private final @NotNull String namePrefixFix;

    private final @NotNull Map<String, Value> values = new LinkedHashMap<>();

    public EnumType(
            @NotNull String name,
            @Nullable String comment
    ) {
        this.name = name;
        this.comment = comment;

        Pattern wordExtractor = Pattern.compile("^(?<word>[A-Z]+[a-z0-9]*)([A-Z]|$)");

        List<String> nameWords = new ArrayList<>();

        while (!name.isBlank()) {
            Matcher matcher = wordExtractor.matcher(name);
            if(!matcher.find()) break;
            String word = matcher.group("word");

            if(!word.equals("KHR")) nameWords.add(word);
            name = name.substring(word.length());
        }

        namePrefixToIgnore = nameWords.stream().reduce((cur, add) -> cur.toUpperCase(Locale.ROOT) + "_" + add.toUpperCase(Locale.ROOT)).orElse("") + "_";
        namePrefixFix = nameWords.get(nameWords.size()-1).toUpperCase(Locale.ROOT) + "_";
        LOG.debug("enumName: " + this.name + ", namePrefixToIgnore: " + namePrefixToIgnore);
    }

    public String getEnumValueName(String vkName) {
        if(vkName.startsWith(namePrefixToIgnore))
            vkName = vkName.substring(namePrefixToIgnore.length());

        if(Pattern.compile("^\\d").matcher(vkName).find()) {
            vkName = namePrefixFix + vkName;
        }

        return vkName;
    }

    public void addValue(@NotNull Node enumNode) {
        if(enumNode.getAttributes() == null) {
            throw new IllegalStateException("<enum> node without any attributes: " + enumNode.getTextContent());
        }

        Node nameAttr = enumNode.getAttributes().getNamedItem("name");
        Node valueAttr = enumNode.getAttributes().getNamedItem("value");
        Node aliasAttr = enumNode.getAttributes().getNamedItem("alias");
        Node commentAttr = enumNode.getAttributes().getNamedItem("comment");
        Node deprecatedAttr = enumNode.getAttributes().getNamedItem("deprecated");

        if(nameAttr == null || (valueAttr == null && aliasAttr == null))
            throw new IllegalStateException("<enum> node without name or value/alias: " + enumNode.getTextContent());

        Value v = new Value(
                nameAttr.getNodeValue(),
                getEnumValueName(nameAttr.getNodeValue()),
                valueAttr == null ? getEnumValueName(aliasAttr.getNodeValue()) + ".getValue()" : valueAttr.getNodeValue(),
                commentAttr == null ? null : commentAttr.getNodeValue(),
                deprecatedAttr == null ? null : deprecatedAttr.getNodeValue(),
                null);


        values.put(v.name, v);

    }

    public void addValue(@NotNull Value value) {
        values.put(value.name, value);
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull TypeType getType() {
        return TypeType.ENUM;
    }



    @Override
    public void generate(@NotNull RegistryLoader registry, @NotNull SourceGenerator generator) {
        var clazz = generator.addJavaFile(SUB_PACKAGE);
        clazz.setName(name);
        clazz.setType(JavaClassType.ENUM);
        clazz.setVisibility(JavaVisibility.PUBLIC);
        if(comment != null) clazz.setJavaDoc(comment, true);
        clazz.setImplementedClasses(new JavaClass[]{JavaClass.ofClass(NativeEnumMember32.class)});
        var valueVar = clazz.addVariable(JavaClass.ofClass(int.class), "value");
        valueVar.setFinal(true);
        valueVar.setVisibility(JavaVisibility.PRIVATE);
        var method = clazz.addGetter(valueVar);
        method.addAnnotation(JavaClass.ofClass(Override.class));
        method.setVisibility(JavaVisibility.PUBLIC);
        var constructor = clazz.addConstructor();
        var valueParameter = constructor.addParameter("valueParam", JavaClass.ofClass(int.class));
        constructor.body(body -> body.addExpression(JavaExpression.assign(valueVar, valueParameter)));

        // Sort, so that aliases are in last place.
        var sortedVals = values.values().stream().sorted(
                (o1, o2) -> {
                    boolean bo1 = o1.stringValue.contains(".getValue()");
                    boolean bo2 = o2.stringValue.contains(".getValue()");
                    if(bo1 && !bo2) return 1;
                    else if(!bo1 && bo2) return -1;
                    return 0;
                }
        ).collect(Collectors.toCollection(ArrayList::new));
        for (Value value : sortedVals) {
            var member = clazz.addEnumMember(value.name, JavaExpression.ofCode(value.stringValue));
            if(value.deprecated != null)
                member.addAnnotation(JavaClass.ofClass(Deprecated.class))
                        .setValue(
                                JavaVariable.of(Deprecated.class, "since"),
                                JavaExpression.ofString(value.deprecated)
                        );

            var doc = member.setJavaDoc(value.comment == null ? "" : value.comment, true);
            if(value.writeDoc != null)
                value.writeDoc.accept(doc);
        }
    }

    @Override
    public @NotNull CTypes getAsBaseType() {
        return CTypes.INT32;
    }

    @Override
    public @NotNull JavaClass getJavaClass(@NotNull RegistryLoader registry, @NotNull SourceGenerator generator) {
        return new JavaClass() {
            @Override
            public @NotNull JavaPackage getPackage() {
                return generator.getJavaBasePackage().extend(SUB_PACKAGE);
            }

            @Override
            public @NotNull String getName() {
                return name;
            }
        };
    }

    public static class Value {
        private final @NotNull String vkName;
        private final @NotNull String name;
        private final @NotNull String stringValue;
        private final @Nullable String comment;
        private final @Nullable String deprecated;
        private final @Nullable Consumer<JavaDocGenerator> writeDoc;

        public Value(@NotNull String vkName, @NotNull String name, @NotNull String stringValue, @Nullable String comment, @Nullable String deprecated, @Nullable Consumer<JavaDocGenerator> writeDoc) {
            this.vkName = vkName;
            this.name = name;
            this.stringValue = stringValue;
            this.comment = comment;
            this.deprecated = deprecated;
            this.writeDoc = writeDoc;
        }
    }
}
