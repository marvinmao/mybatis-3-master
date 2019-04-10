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
package org.apache.ibatis.submitted.javassist;

import javassist.util.proxy.Proxy;
import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Reader;

import static org.junit.Assert.assertTrue;

public class JavassistTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        // create a SqlSessionFactory
        try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/javassist/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }

        // populate in-memory database
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                "org/apache/ibatis/submitted/javassist/CreateDB.sql");
    }

    @Test
    public void shouldGetAUserAndGroups() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Mapper mapper = sqlSession.getMapper(Mapper.class);
            User user = mapper.getUser(1);
            Assert.assertEquals("User1", user.getName());
            assertTrue(user instanceof Proxy);
            Assert.assertEquals(1, user.getGroups().size());
        }
    }

}