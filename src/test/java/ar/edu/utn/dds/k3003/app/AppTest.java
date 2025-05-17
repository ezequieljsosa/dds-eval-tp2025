package ar.edu.utn.dds.k3003.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.dds.k3003.ClassFinder;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {
  public static final String CLASS_FULL_NAME = "ar.edu.utn.dds.k3003.app.Fachada";

  private FachadaAgregador fachadaColaboradores;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    fachadaColaboradores =
        ClassFinder.findAndInstantiateClassImplementingInterface(
            CLASS_FULL_NAME, FachadaAgregador.class);
  }

  @Test
  void testInstaciacion() {
    assertEquals(Fachada.class, fachadaColaboradores.getClass());
  }
}
