package ar.edu.utn.dds.k3003.tests.pdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
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
public class PdITest implements TestTP<FachadaProcesadorPdI> {

  private static final String UN_HECHO_ID = "unHechoId";
  public static final PdIDTO PDI = new PdIDTO("", UN_HECHO_ID, "una info", "bsas",
      LocalDateTime.now(), "1234556", List.of());
  private static final String PDI_ID = "unPDIid";

  private FachadaProcesadorPdI instancia;
  private PdIDTO pdi1;
  private PdIDTO pdi2;

  @Mock
  FachadaSolicitudes fachadaSolicitudes;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    instancia = this.instance();
    instancia.setFachadaSolicitudes(fachadaSolicitudes);
  }

  @Test
  @DisplayName("Procesar PdIs")
  void testProcesarPdI() {

    when(fachadaSolicitudes.estaActivo(UN_HECHO_ID)).thenReturn(true);

    val pdi1 = instancia.procesar(PDI);
    instancia.procesar(PDI);

    assertNotNull(pdi1.id(), "El PdI deberia tener un identificador no nulo");
    assertEquals(pdi1.hechoId(), instancia.buscarPdIPorId(pdi1.id()).hechoId(),
        "No se esta recuperando el PdI correctamente");
    assertEquals(2, instancia.buscarPorHecho(PDI_ID).size(), "No se estan sumando correctamente los PdIs");

  }

  @Test
  @DisplayName("Procesar PdI que fue borrado")
  void testProcesarPdICerrado() {

    when(fachadaSolicitudes.estaActivo(UN_HECHO_ID)).thenReturn(false);

    assertThrows( IllegalStateException.class, () -> instancia.procesar(PDI));

  }

  @Override
  public String paquete() {
    return PAQUETE_BASE + "tests.pdi";
  }

  @Override
  public Class<FachadaProcesadorPdI> clase() {
    return FachadaProcesadorPdI.class;
  }
}
