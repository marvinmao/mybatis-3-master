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
package org.apache.ibatis.submitted.mapper_extend;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Reader;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;

public class MapperExtendTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void setUp() throws Exception {
        // create an SqlSessionFactory
        try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/mapper_extend/mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }

        // populate in-memory database
        BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
                "org/apache/ibatis/submitted/mapper_extend/CreateDB.sql");
    }

    @Test
    public void shouldGetAUserWithAnExtendedXMLMethod() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ParentMapper mapper = sqlSession.getMapper(Mapper.class);
            User user = mapper.getUserXML();
            Assert.assertEquals("User1", user.getName());
        }
    }


    @Test
    public void shouldGetAUserWithAnExtendedAnnotatedMethod() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ParentMapper mapper = sqlSession.getMapper(Mapper.class);
            User user = mapper.getUserAnnotated();
            Assert.assertEquals("User1", user.getName());
        }
    }

    @Test
    public void shouldGetAUserWithAnOverloadedXMLMethod() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ParentMapper mapper = sqlSession.getMapper(MapperOverload.class);
            User user = mapper.getUserXML();
            Assert.assertEquals("User2", user.getName());
        }
    }

    @Test
    public void shouldGetAUserWithAnOverloadedAnnotatedMethod() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ParentMapper mapper = sqlSession.getMapper(MapperOverload.class);
            User user = mapper.getUserAnnotated();
            Assert.assertEquals("User2", user.getName());
        }
    }

    @Test
    public void shouldFindStatementInSubInterfaceOfDeclaringClass() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            ChildMapper mapper = sqlSession.getMapper(ChildMapper.class);
            User user = mapper.getUserByName("User1");
            Assert.assertNotNull(user);
        }
    }

    @Test
    public void shouldThrowExceptionIfNoMatchingStatementFound() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Mapper mapper = sqlSession.getMapper(Mapper.class);
            when(mapper).noMappedStatement();
            then(caughtException()).isInstanceOf(BindingException.class)
                    .hasMessage("Invalid bound statement (not found): "
                            + Mapper.class.getName() + ".noMappedStatement");
        }
    }
}
