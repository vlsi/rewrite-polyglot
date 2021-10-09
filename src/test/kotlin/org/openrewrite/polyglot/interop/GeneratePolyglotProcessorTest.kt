/*
 *  Copyright 2021 the original author or authors.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  https://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openrewrite.polyglot.interop

import org.joor.CompileOptions
import org.joor.Reflect
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.spi.FileSystemProvider

@Disabled
class GeneratePolyglotProcessorTest {

    @Test
    fun mustGenerateTypeScriptTypes() {
        val p = GeneratePolyglotProcessor()
        FileSystemProvider.installedProviders()

        val test = Reflect.compile(
            "org.openrewrite.test.TestRecipe",
            //language=java
            """
            package org.openrewrite.test;
            
            import org.openrewrite.*;
            import org.openrewrite.internal.lang.Nullable;
            import org.openrewrite.json.JsonIsoVisitor;
            
            public class TestRecipe extends Recipe {
                @Option(displayName = "Option One",
                        description = "First of two options.",
                        example = "something")
                String optionOne;
                       
                @Option(displayName = "Option Two",
                        description = "Second of two options.",
                        required = false,
                        example = "*")
                @Nullable
                String optionTwo;
                           
                public TestRecipe(String optionOne, @Nullable String optionTwo) {
                    this.optionOne = optionOne;
                    this.optionTwo = optionTwo;
                }

                @Override
                public String getDisplayName() {
                    return "Test Recipe";
                }
            
                @Override
                public String getDescription() {
                    return "Test the polyglot interop support.";
                }
            
                @Override
                protected TreeVisitor<?, ExecutionContext> getSingleSourceApplicableTest() {
                    return new TreeVisitor<>() {};
                }
            
                @Override
                public JsonIsoVisitor<ExecutionContext> getVisitor() {
                    return new JsonIsoVisitor<>() {};
                }
            }
            """.trimIndent(),
            CompileOptions().processors(p)
        ).create("optionOne", null)

        test.get<Any>()
    }

}