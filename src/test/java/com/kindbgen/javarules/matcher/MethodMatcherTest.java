/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kindbgen.javarules.matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.sonar.java.model.JParser;
import org.sonar.java.model.JParserConfig;
import org.sonar.java.model.JavaVersionImpl;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.ExpressionStatementTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.StatementTree;

public class MethodMatcherTest {

    public static final String CLASSES_FILEPATH = "target/classes";
    public static final String TEST_CLASSES_FILEPATH = "target/test-classes";
    public static final String JAVA_VERSION = "1.8";
    public static final String UNIT_NAME = "test";
    private static final String CODE_TO_PARSE_METHOD_SELECT_KIND_IDENTIFIER = "package com.kindbgen.test; "
            + "class MyClass1 {}"
            + "class MyClass2 {}"
            + "class TestClass { "
            + "void test(MyClass1 m1, MyClass2 m2){}"
            + "void bar(){MyClass1 m1 = new MyClass1(); MyClass2 m2 = new MyClass2(); test(m1, m2);} "
            + "}";
    private static final String CODE_TO_PARSE_METHOD_SELECT_KIND_MEMBER_SELECT = "package com.kindbgen.test; "
            + "class MyClass1 {}"
            + "class MyClass2 {}"
            + "class TestClass { "
            + "void test(MyClass1 m1, MyClass2 m2){}"
            + "void bar(){MyClass1 m1 = new MyClass1(); MyClass2 m2 = new MyClass2(); this.test(m1, m2);} "
            + "}";
    private static final int CLASS_INDEX = 2;
    private static final int CLASS_METHOD_INDEX = 1;
    private static final int METHOD_INVOCATION_INDEX = 2;
    private MethodInvocationTree methodInvocationTree;

    @Test
    void shouldMatchMethodWhenMethodNameAndOwnerClassAndMethodParametersMatch() {
        givenMethodInvocationTree(CODE_TO_PARSE_METHOD_SELECT_KIND_IDENTIFIER);

        MethodMatcher methodMatcher = MethodMatcher.create(
                MethodNamePredicate.is(UNIT_NAME),
                OwnerTypePredicate.is("com.kindbgen.test.TestClass"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass1"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass2"));

        assertThat(methodMatcher.matches(methodInvocationTree), is(true));
    }

    @Test
    void shouldMatchMethodWhenMethodNameAndOwnerClassAndOnlySecondParameterMatch() {
        givenMethodInvocationTree(CODE_TO_PARSE_METHOD_SELECT_KIND_MEMBER_SELECT);

        MethodMatcher methodMatcher = MethodMatcher.create(
                MethodNamePredicate.is(UNIT_NAME),
                OwnerTypePredicate.is("com.kindbgen.test.TestClass"),
                ParameterTypePredicate.anyParameterType(),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass2"));

        assertThat(methodMatcher.matches(methodInvocationTree), is(true));
    }

    @Test
    void shouldNotMatchMethodWhenMethodNameDoesNotMatch() {
        givenMethodInvocationTree(CODE_TO_PARSE_METHOD_SELECT_KIND_IDENTIFIER);

        MethodMatcher methodMatcher = MethodMatcher.create(
                MethodNamePredicate.is("different"),
                OwnerTypePredicate.is("com.kindbgen.test.TestClass"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass1"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass2"));

        assertThat(methodMatcher.matches(methodInvocationTree), is(false));
    }

    @Test
    void shouldNotMatchMethodWhenNumberOfMethodParametersDoesNotMatch() {
        givenMethodInvocationTree(CODE_TO_PARSE_METHOD_SELECT_KIND_MEMBER_SELECT);

        MethodMatcher methodMatcher = MethodMatcher.create(
                MethodNamePredicate.is(UNIT_NAME),
                OwnerTypePredicate.is("com.kindbgen.test.TestClass"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass1"));

        assertThat(methodMatcher.matches(methodInvocationTree), is(false));
    }

    @Test
    void shouldNotMatchMethodWhenMethodOwnerClassDoesNotMatch() {
        givenMethodInvocationTree(CODE_TO_PARSE_METHOD_SELECT_KIND_IDENTIFIER);

        MethodMatcher methodMatcher = MethodMatcher.create(
                MethodNamePredicate.is(UNIT_NAME),
                OwnerTypePredicate.is("com.kindbgen.test.Different"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass1"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass2"));

        assertThat(methodMatcher.matches(methodInvocationTree), is(false));
    }

    @Test
    void shouldNotMatchMethodWhenMethodParameterTypesDoNotMatch() {
        givenMethodInvocationTree(CODE_TO_PARSE_METHOD_SELECT_KIND_MEMBER_SELECT);

        MethodMatcher methodMatcher = MethodMatcher.create(
                MethodNamePredicate.is(UNIT_NAME),
                OwnerTypePredicate.is("com.kindbgen.test.TestClass"),
                ParameterTypePredicate.is("com.kindbgen.test.Different1"),
                ParameterTypePredicate.is("com.kindbgen.test.MyClass2"));

        assertThat(methodMatcher.matches(methodInvocationTree), is(false));
    }

    private void givenMethodInvocationTree(String codeToParse) {
        CompilationUnitTree compilationUnitTree = parse(codeToParse);
        ClassTree classTree = (ClassTree) compilationUnitTree.types().get(CLASS_INDEX);
        StatementTree statementTree = ((MethodTree) classTree.members().get(CLASS_METHOD_INDEX))
                .block()
                .body()
                .get(METHOD_INVOCATION_INDEX);
        this.methodInvocationTree = (MethodInvocationTree) ((ExpressionStatementTree) statementTree).expression();
    }

    private CompilationUnitTree parse(String source) {
        List<File> classpath = Arrays.asList(new File(TEST_CLASSES_FILEPATH), new File(CLASSES_FILEPATH));
        return JParser.parse(
                JParserConfig.Mode.FILE_BY_FILE
                        .create(new JavaVersionImpl(11), classpath)
                        .astParser(),
                JAVA_VERSION,
                UNIT_NAME,
                source);
    }
}
