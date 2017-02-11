package eu.benonline.web;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

/**
 * Created by Benjamin Peter.
 */
@RunWith(SpringRunner.class)
abstract class AbstractRestControllerTest {

    final MediaType contentType = MediaType.APPLICATION_JSON_UTF8;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    String toJsonString(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        mappingJackson2HttpMessageConverter.write(o, contentType, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
