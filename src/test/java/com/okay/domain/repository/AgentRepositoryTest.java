//package com.okay.domain.repository;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.transaction.Transactional;
//
//@RunWith(SpringRunner.class)
//@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/applicationContext.xml"})
//@Transactional
//@Rollback(value = false)
//public class AgentRepositoryTest {
//    @Autowired
//    private AgentRepository agentRepository = null;
//
//    @Test
//    public void test1(){
//        Agent agent = Agent.builder()
//                .id(2L)
//                .name("Hello")
//                .build();
//        System.out.println(agentRepository);
//         agentRepository.save(agent);
//    }
//}