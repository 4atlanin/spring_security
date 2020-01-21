package by.security.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController
{
    @PreAuthorize( "hasRole('ROLE_ADMIN')" )
    @GetMapping( path = "/only/admin" )
    public String protectedAdmin()
    {
        return "onlyAdmin";
    }

    @GetMapping( path = "/guest" )
    public String guestAndAdmin()
    {
        return "guestAndAdmin";
    }

    @GetMapping( path = "/permit-all" )
    public String freeAccess()
    {
        return "freeAccess";
    }
}
