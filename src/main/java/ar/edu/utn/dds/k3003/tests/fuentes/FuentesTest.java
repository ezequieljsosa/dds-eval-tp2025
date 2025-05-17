package ar.edu.utn.dds.k3003.tests.viandas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.time.LocalDateTime;
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
public class FuentesTest implements TestTP<FachadaFuente> {

  private static final String UNA_COLECCION = "unaColeccion";
  private static final String UN_TITULO = "unHecho";
  private static final String PDI_ID = "unPdIId";

  private FachadaFuente instancia;

  @Mock
  private FachadaProcesadorPdI fachadaProcesadorPdI;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    instancia = this.instance();
    instancia.setProcesadorPdI(fachadaProcesadorPdI);
  }

  @Test
  @DisplayName("Agregar coleccion")
  void testAgregarColeccion() {

    val coleccion = instancia.agregar(new ColeccionDTO(UNA_COLECCION, "coleccion"));

    val coleccionXId = instancia.buscarColeccionXId(coleccion.nombre());

    assertEquals(UNA_COLECCION, coleccionXId.nombre());

  }

  @Test
  @DisplayName("Agregar coleccion nombre repetido")
  void testIntentarAgregarColeccion() {

    instancia.agregar(new ColeccionDTO(UNA_COLECCION, "coleccion"));

    assertThrows(IllegalArgumentException.class,
        () -> instancia.agregar(new ColeccionDTO(UNA_COLECCION, "coleccion")));
  }

  @Test
  @DisplayName("Agregar hecho")
  void testAgregarHecho() {

    instancia.agregar(new ColeccionDTO(UNA_COLECCION, "coleccion"));
    val hechoDTO = new HechoDTO("", UN_TITULO, UNA_COLECCION, List.of("etiqueta1"),
        CategoriaHechoEnum.ENTRETENIMIENTO,
        "bsas", LocalDateTime.now(), "celular");

    val hecho1 = instancia.agregar(hechoDTO);

    assertNotNull(hecho1.id(), "No se asigno un identificador al hecho agregado");

    val hecho2 = instancia.buscarHechoXId(hecho1.id());

    assertEquals(UN_TITULO, hecho2.titulo(),
        "Al buscar por id de hecho no se retorna el correcto.");

  }

  @Test
  @DisplayName("Agregar un pdi a un hecho")
  void testAgregarPdiAHecho() {

    when(fachadaProcesadorPdI.procesar(any(PdIDTO.class))).thenReturn(
        new PdIDTO("1", UN_TITULO));

    instancia.agregar(new ColeccionDTO(UNA_COLECCION, "coleccion"));
    val hechoDTO = new HechoDTO("", UN_TITULO, UNA_COLECCION, List.of(),
        CategoriaHechoEnum.ENTRETENIMIENTO,
        "bsas", LocalDateTime.now(), "celular");
    val hecho1 = instancia.agregar(hechoDTO);

    instancia.agregar(new PdIDTO(PDI_ID, hecho1.id()));

  }

  @Test
  @DisplayName("Agregar un pdi a un hecho")
  void testTratarDeAgregarHecho() {

    when(fachadaProcesadorPdI.procesar(any(PdIDTO.class))).thenThrow(IllegalStateException.class);

    instancia.agregar(new ColeccionDTO(UNA_COLECCION, "coleccion"));
    val hechoDTO = new HechoDTO("", UN_TITULO, UNA_COLECCION, List.of(),
        CategoriaHechoEnum.ENTRETENIMIENTO,
        "bsas", LocalDateTime.now(), "celular");
    val hecho1 = instancia.agregar(hechoDTO);

    assertThrows(IllegalStateException.class,
        () -> instancia.agregar(new PdIDTO(PDI_ID, hecho1.id())));

  }

  @Override
  public String paquete() {
    return PAQUETE_BASE + "tests.fuentes";
  }

  @Override
  public Class<FachadaFuente> clase() {
    return FachadaFuente.class;
  }
}
