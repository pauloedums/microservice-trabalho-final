package br.com.impacta.microservices.ib;

import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
        title="Bank API",
        version = "1.0",
        contact = @Contact(
                name = "bank API Support",
                url = "http://bankapi.com/contact",
                email = "bankapi@bankapi.com"),
        license = @License(
                name = "Apache 2.0",
                url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ClientAPIAplication extends Application {}