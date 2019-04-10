/**
 * Copyright ${license.git.copyrightYears} the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.type;

import org.apache.ibatis.domain.misc.RichType;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleTypeRegistryTest {

    @Test
    public void shouldTestIfClassIsSimpleTypeAndReturnTrue() {
        assertTrue(SimpleTypeRegistry.isSimpleType(String.class));
    }

    @Test
    public void shouldTestIfClassIsSimpleTypeAndReturnFalse() {
        assertFalse(SimpleTypeRegistry.isSimpleType(RichType.class));
    }

    @Test
    public void shouldTestIfMapIsSimpleTypeAndReturnFalse() {
        assertFalse(SimpleTypeRegistry.isSimpleType(HashMap.class)); // see issue #165, a Map is not a simple type
    }

}