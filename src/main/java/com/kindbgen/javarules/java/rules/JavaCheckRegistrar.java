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
package com.kindbgen.javarules.java.rules;

import org.sonar.api.config.Configuration;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonarsource.api.sonarlint.SonarLintSide;

import java.util.List;
import java.util.stream.Collectors;

import static com.kindbgen.javarules.java.Constants.REPOSITORY_KEY;

@SonarLintSide
public class JavaCheckRegistrar implements CheckRegistrar {


    public JavaCheckRegistrar(Configuration configuration) {
    }

    @Override
    public void register(RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(REPOSITORY_KEY, checkClasses(), testCheckClasses());
    }

    private List<Class<? extends JavaCheck>> checkClasses() {
        return JavaRulesList.getJavaChecks().stream()
                .collect(Collectors.toList());
    }

    private List<Class<? extends JavaCheck>> testCheckClasses() {
        return JavaRulesList.getJavaTestChecks().stream()
                .collect(Collectors.toList());
    }

}
