package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

public class BeerControllerTest {

    @Mock
    private BeerService beerService;

    private MockMvc mockMvc;

    @InjectMocks
    private BeerController beerController;

    private static final String BEER_URL = "/api/v1/beer";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beerController).build();
    }

    @Test
    public void testGetBeer() throws Exception {
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_URL + "/" + UUID.randomUUID()))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        BDDMockito.then(beerService).should().getBeerById(ArgumentMatchers.any());
    }

    @Test
    public void testHandlePost() throws Exception {
        // given
        BeerDto beerDto = BeerDto.builder()
                .id(null)
                .beerName("Test Beer")
                .beerStyle("Lager")
                .upc(Long.valueOf(5)).build();

        BeerDto returnBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle("Pale Ale")
                .build();

        BDDMockito.given(beerService.save(ArgumentMatchers.any(BeerDto.class))).willReturn(returnBeer);

        // when - then
        mockMvc.perform(MockMvcRequestBuilders.post(BEER_URL, beerDto)
            .content(objectMapper.writeValueAsString(beerDto))
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        BDDMockito.then(beerService).should().save(ArgumentMatchers.any(BeerDto.class));

    }

    @Test
    public void testHandlePut() throws Exception {
        // given
        BeerDto beerDto = BeerDto.builder()
                .id(null)
                .beerName("Test Beer")
                .beerStyle("Lager")
                .upc(Long.valueOf(5)).build();

        // when - then
        mockMvc.perform(MockMvcRequestBuilders.put(BEER_URL + "/" + UUID.randomUUID(), beerDto)
                .content(objectMapper.writeValueAsString(beerDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(beerService).should().updateBeer(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(BeerDto.class));
    }

   @Test
    public void testDeleteBeer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_URL + "/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(beerService).should().deleteBeer(ArgumentMatchers.any(UUID.class));
    }

   @Test
    public void saveInvalidBeer() throws Exception{
        // given
        BeerDto beerDto = BeerDto.builder()
                .id(null)
                .beerStyle("Lager")
                .upc(Long.valueOf(5)).build();

        BeerDto returnBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle("Pale Ale")
                .build();

        // when - then
        mockMvc.perform(MockMvcRequestBuilders.post(BEER_URL, beerDto)
                .content(objectMapper.writeValueAsString(beerDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        BDDMockito.then(beerService).shouldHaveZeroInteractions();
    }

}