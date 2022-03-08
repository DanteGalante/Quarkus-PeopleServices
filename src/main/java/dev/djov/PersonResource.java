package dev.djov;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dev.djov.db.PersonRepository;

@Path("/people")
public class PersonResource {

    private final PersonRepository personRepository;

    public PersonResource(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> all() {
        System.out.println("GET>>Obtener una lista de todos las personas de la bd");
        return personRepository.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person get(@PathParam("id") UUID id) {
        System.out.println("GET>>Buscar a una persona segun su id");
        return personRepository.findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Person post(Person person) {
        System.out.println("POST>>Crear persona");
        return personRepository.insert(
            new Person(UUID.randomUUID(), person.getName(), person.getAge())
        );
    }

}