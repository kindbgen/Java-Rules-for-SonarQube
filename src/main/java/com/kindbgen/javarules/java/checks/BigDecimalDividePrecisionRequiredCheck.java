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
package com.kindbgen.javarules.java.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;

import java.util.List;

/**
 * @author <a href="mailto:kindbgen@gmail.com">Kindbgen<a/>
 * @description BigDecimal.divide 必须指定精度
 * @date 2025/5/16
 */
@Rule(key = BigDecimalDividePrecisionRequiredCheck.RULE_KEY)
public class BigDecimalDividePrecisionRequiredCheck extends BaseTreeVisitor implements JavaFileScanner {

    public static final String RULE_KEY = "S8888";
    public static final String RULE_MESSAGE = "\"BigDecimal.divide()\" must specify precision and rounding mode";
    private static final String BIG_DECIMAL_FQN = "java.math.BigDecimal";
    private static final String METHOD_NAME = "divide";
    private JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitMethodInvocation(MethodInvocationTree tree) {
        // 检查方法名是否为 divide
        if (METHOD_NAME.equals(tree.methodSymbol().name())) {
            // 验证是否为 BigDecimal.divide 方法
            if (tree.methodSymbol().owner().type().is(BIG_DECIMAL_FQN)) {
                checkDivideArguments(tree);
            }
        }
        super.visitMethodInvocation(tree);
    }

    private void checkDivideArguments(MethodInvocationTree tree) {
        List<ExpressionTree> args = tree.arguments();
        // 检查参数数量：必须 >= 2（精度+舍入模式）
        if (args.size() < 2) {
            context.reportIssue(this, tree, "必须指定精度和舍入模式");
        } else {
            // 验证第二个参数是否为整数（精度）
            ExpressionTree scaleArg = args.get(1);
            if (!scaleArg.symbolType().is("int")) {
                context.reportIssue(this, scaleArg, "精度参数必须是整数");
            }
        }
    }
}