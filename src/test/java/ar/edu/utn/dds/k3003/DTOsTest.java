package ar.edu.utn.dds.k3003;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DTOsTest {



  @Test
  void testRetiroDTOEquals() {
    val col1 = new ColeccionDTO("333", "321");
    val col2 = new ColeccionDTO("333", "321");
    assertEquals(col1, col2);
  }
}
