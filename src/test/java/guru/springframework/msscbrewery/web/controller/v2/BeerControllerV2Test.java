package guru.springframework.msscbrewery.web.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import guru.springframework.msscbrewery.web.model.v2.BeerStyleEnum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

public class BeerControllerV2Test {

    @Mock
    private BeerServiceV2 beerServiceV2;

    private MockMvc mockMvc;

    @InjectMocks
    private BeerControllerV2 beerControllerV2;

    private static final String BEER_URL = "/api/v2/beer";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beerControllerV2).build();
    }

    @Test
    public void testGetBeer() throws Exception {

        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_URL + "/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        BDDMockito.then(beerServiceV2).should().getBeerById(ArgumentMatchers.any());
    }

    @Test
    public void testHandlePost() throws Exception {
        // given
        BeerDtoV2 beerDto = BeerDtoV2.builder()
                .id(null)
                .beerName("Test Beer")
                .beerStyle(BeerStyleEnum.IPA)
                .upc(Long.valueOf(5)).build();

        BeerDtoV2 returnBeer = BeerDtoV2.builder()
                .id(UUID.randomUUID())
                .beerName("Kormoran")
                .beerStyle(BeerStyleEnum.IPA)
                .build();

        BDDMockito.given(beerServiceV2.save(ArgumentMatchers.any(BeerDtoV2.class))).willReturn(returnBeer);

        // when - then
        mockMvc.perform(MockMvcRequestBuilders.post(BEER_URL, beerDto)
                .content(objectMapper.writeValueAsString(beerDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.then(beerServiceV2).should().save(ArgumentMatchers.any(BeerDtoV2.class));

    }

    @Test
    public void testHandlePut() throws Exception {
        // given
        BeerDtoV2 beerDto = BeerDtoV2.builder()
                .id(null)
                .beerName("Test Beer")
                .beerStyle(BeerStyleEnum.IPA)
                .upc(Long.valueOf(5)).build();

        // when - then
        mockMvc.perform(MockMvcRequestBuilders.put(BEER_URL + "/" + UUID.randomUUID(), beerDto)
                .content(objectMapper.writeValueAsString(beerDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(beerServiceV2).should().updateBeer(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(BeerDtoV2.class));
    }

    @Test
    public void testDeleteBeer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_URL + "/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(beerServiceV2).should().deleteBeer(ArgumentMatchers.any(UUID.class));
    }
}