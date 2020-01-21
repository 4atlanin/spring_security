package by.security.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.digest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DigestAndBasicApplicationTests
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testNoAccessWithoutAuthentication() throws Exception
    {
        String responseBody = mockMvc.perform( get( "/only/admin" ) )
                                     .andDo( print() )
                                     .andExpect( status().isUnauthorized() ).andReturn().getResponse().getContentAsString().trim();
        assertEquals( "My Own HTTP Status 401 - Full authentication is required to access this resource", responseBody );  // Респонс был сделан в MyBasicAuthenticationEntryPoint
    }

    @Test
    void testAccessToNotProtectedEndpointWithoutAuthentication() throws Exception
    {
        String responseBody = mockMvc.perform( get( "/permit-all" ) )
                                     .andDo( print() )
                                     .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString().trim();
        assertEquals( "freeAccess", responseBody );
    }

    @Test
    void testAdminHasAccessEverywhere() throws Exception
    {
        String responseBody = mockMvc.perform( get( "/only/admin" )
            .with( httpBasic( "admin", "admin" ) ) )
                                     .andDo( print() )
                                     .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString().trim();

        assertEquals( "onlyAdmin", responseBody );

    }

    @Test
    void testAdminHasAccessGuestPage() throws Exception
    {
        String responseBody = mockMvc.perform( get( "/guest" )
            .with( httpBasic( "admin", "admin" ) ) )
                                     .andDo( print() )
                                     .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString().trim();

        assertEquals( "guestAndAdmin", responseBody );

    }

    @Test
    void testAdminHasAccessNotProtectedPage() throws Exception
    {
        String responseBody = mockMvc.perform( get( "/permit-all" )
            .with( httpBasic( "admin", "admin" ) ) )
                                     .andDo( print() )
                                     .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString().trim();

        assertEquals( "freeAccess", responseBody );

    }

    @Test
    void testGuestHasNoAccessToAdminEndpoint() throws Exception
    {
        mockMvc.perform( get( "/only/admin" )
            .with( httpBasic( "guest", "guest" ) ) )
               .andDo( print() )
               .andExpect( status().isForbidden() ).andReturn().getResponse().getContentAsString().trim();
    }

    /**
     * Digest Authentication
     *
     * @throws Exception
     */
    @Test
    void testGuestHasAccessToAdminToGuestEndpoint() throws Exception
    {
        String responseBody = mockMvc.perform( get( "/guest" )
            .with( digest( "guest" ).password( "guest" ).realm( "digestRealm" ) ) )
                                     .andDo( print() )
                                     .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString().trim();
        assertEquals( "guestAndAdmin", responseBody );
    }

}
