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
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalDividePrecisionRequiredCheck {

    public void badDivide() {
        BigDecimal a = BigDecimal.valueOf(1);
        BigDecimal b = BigDecimal.valueOf(3);

        // 未指定精度（触发规则）
//        BigDecimal error1 = a.divide(b);

        // 仅指定舍入模式（参数不足）
        BigDecimal error2 = a.divide(b, BigDecimal.ROUND_DOWN);
    }

    public void goodDivide() {
        BigDecimal a = BigDecimal.valueOf(1);
        BigDecimal b = BigDecimal.valueOf(3);

        // 新版 RoundingMode
        BigDecimal success1 = a.divide(b, 2, RoundingMode.DOWN);

        // 旧版常量
        BigDecimal success2 = a.divide(b, 2, BigDecimal.ROUND_DOWN);

        // 使用 MathContext（兼容另一种重载）
        BigDecimal success3 = a.divide(b, new MathContext(2));
    }

}
