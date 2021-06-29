//package com.okay.service;
//
//import com.okay.domain.entity.Agent;
//import com.okay.domain.repository.AgentRepository;
//import com.okay.dto.AgentDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import java.util.List;
//
//
////@ContextConfiguration(locations = "../../../../webapp/WEB-INF/applicationContext.xml")
////@WebAppConfiguration
//@AutoConfigureMockMvc
//class AgentServiceTest {
//    @Mock
//    AgentService agentService;
//    @Mock
//    AgentRepository agentRepository;
////
////    @Mock
////    AgentRepository agentRepository;
//
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.initMocks(this);
//        agentService = new AgentService(agentRepository);
//    }
//
//    @Test
//    public void 사용자_추가(){
//        AgentDto agentDto = AgentDto.builder()
//                .id(1L)
//                .name("Danny")
//                .build();
//
//        Agent agent = agentDto.changeAgent(agentDto);
//        System.out.println(agent);
//        System.out.println("+++++++++");
//        System.out.println(agentRepository);
//        System.out.println("+++++++++");
//        System.out.println(agentService);
//
//    }
//
//
//    @Test
//    public void test(){
//        List<Agent> result = agentRepository.findAll();
//        System.out.println(result);
//    }
//}