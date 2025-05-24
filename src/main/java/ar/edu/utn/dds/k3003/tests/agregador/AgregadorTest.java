package ar.edu.utn.dds.k3003.tests.agregador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.util.List;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AgregadorTest implements TestTP<FachadaAgregador> {

  private static final String NOMBRE_FUENTE = "pepe";

  FachadaAgregador instancia;
  @Mock
  FachadaFuente fuente1;
  @Mock
  FachadaFuente fuente2;


  @SneakyThrows
  @BeforeEach
  void setUp() {
    instancia = this.instance();

  }

  @Test
  @DisplayName("Agregar y buscar colaborador")
  void testAgregarFuente() {

    val fuenteDTO = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    assertNotNull(
        fuenteDTO.id(),
        "#agregar debe retornar un FuenteDTO con un id inicializado.");
    instancia.agregar(new FuenteDTO("", "nombre2", "123"));

    assertEquals(
        2,
        instancia.fuentes().size(),
        "No se esta recuperando el nombre de la fuente correctamente.");

    val fuente1 = instancia.buscarFuenteXId(fuenteDTO.id());
    assertEquals(
        NOMBRE_FUENTE,
        fuente1.nombre(),
        "No se esta recuperando el nombre de la fuente correctamente.");
  }

  @Test
  @DisplayName("Busqueda de hechos usando 2 fuentes y consenso 'TODO'")
  void testBuscarHechosConsensoTodo() {

    initializeFuentes(ConsensosEnum.TODOS);

    val titulos = instancia.hechos("1").stream().map(HechoDTO::titulo).toList();

    assertTrue(
        titulos.containsAll(List.of("a", "b", "c")),
        "El agregador no retorna todos los hechos que deberia para el consenso TODO.");

  }

  @Test
  @DisplayName("Busqueda de hechos usando 2 fuentes y consenso 'Al menos 2'")
  void testBuscarHechosConsensoAlMenos2() {

    initializeFuentes(ConsensosEnum.AL_MENOS_2);

    val titulos = instancia.hechos("1").stream().map(HechoDTO::titulo).toList();

    assertTrue(
        titulos.containsAll(List.of( "b")),
        "El agregador no retorna todos los hechos que deberia para el consenso al menos 2..");

  }

  private void initializeFuentes(ConsensosEnum consenso) {
    val fuenteDTO1 = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    instancia.addFachadaFuentes(fuenteDTO1.id(), fuente1);
    val fuenteDTO2 = instancia.agregar(new FuenteDTO("", NOMBRE_FUENTE, "123"));
    instancia.addFachadaFuentes(fuenteDTO2.id(), fuente2);
    instancia.setConsensoStrategy(consenso, "1");
    when(fuente1.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("1", "1","a"), new HechoDTO("2", "1","b"), new HechoDTO("3", "1","c")
    ));
    when(fuente2.buscarHechosXColeccion("1")).thenReturn(List.of(
        new HechoDTO("4", "1","a"), new HechoDTO("5", "1","b" ), new HechoDTO("6", "2","d")
    ));
  }


  @Override
  public String paquete() {
    return PAQUETE_BASE + "tests.agregador";
  }

  @Override
  public Class<FachadaAgregador> clase() {
    return FachadaAgregador.class;
  }
}
