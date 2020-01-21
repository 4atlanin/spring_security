package by.security.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Как я понял, AuthenticationEntryPoint это тот код, в который попадаешь в случае неудачной Аутентификации.
// У нас случай для Basic

//UP Задачей AuthenticationEntryPoint явялется записать в ответ информацию о том что аутентификация не удалась.
@Slf4j
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint
{
    @Override
    public void afterPropertiesSet()
    {
        setRealmName( "MyRealmName" );
        super.afterPropertiesSet();
    }

    @Override
    public void commence(
        HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx )
        throws IOException
    {
        log.debug( "My BasicAuthenticationEntryPoint. I don't know what to do here." );
        response.addHeader( "WWW-Authenticate", "Basic realm='" + getRealmName() + "'" );
        response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
        try( PrintWriter writer = response.getWriter() )
        {
            writer.println( "My Own HTTP Status 401 - " + authEx.getMessage() );
        }
    }
}
