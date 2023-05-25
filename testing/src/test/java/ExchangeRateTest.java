import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.mockito.Mockito.*;

class ExchangeRateTest {
    @Mock
    private HttpURLConnection httpURLConnectionMock;

    private ExchangeRateTest exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exchangeRateService = new ExchangeRateTest();
    }

    @Test
    void testGetExchangeRate() throws Exception {
        String mockJsonResponse = "{\"result\": 2.5}"; // Example JSON response

        // Mock the URL and connection
        URL urlMock = mock(URL.class);
        whenNew(URL.class).withArguments(anyString()).thenReturn(urlMock);
        when(urlMock.openConnection()).thenReturn(httpURLConnectionMock);

        // Mock the HTTP response
        InputStream mockInputStream = new ByteArrayInputStream(mockJsonResponse.getBytes());
        when(httpURLConnectionMock.getInputStream()).thenReturn(mockInputStream);

        // Call the method
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRate(CurrencyCode.A, CurrencyCode.B);


    }
