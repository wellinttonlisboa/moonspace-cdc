package br.com.moonspace.commons.rest.response.download;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class ApiDownloadTest {

    @Test
    @DisplayName("downloads a file with the given status")
    void shouldAttachFileWhenStatusIsProvided() {
        var download = ApiDownload.of("abc".getBytes(), "report.csv", HttpStatus.ACCEPTED);

        assertThat(download.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(download.getHeaders().getContentDisposition().getFilename()).isEqualTo("report.csv");
        assertThat(download.getBody()).isEqualTo("abc".getBytes());
    }

    @Test
    @DisplayName("downloads a file as OK when no status is given")
    void shouldUseOkWhenStatusIsOmitted() {
        var download = ApiDownload.of("abc".getBytes(), "report.csv");

        assertThat(download.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("falls back to OK when the builder has no status")
    void shouldUseOkWhenBuilderStatusIsNull() {
        var download = ApiDownload.newInstance()
                .withContent("abc".getBytes())
                .withFileName("report.csv")
                .response();

        assertThat(download.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ApiDownload.newInstance().withHttpStatus(HttpStatus.CREATED).withHttpStatus(null)).isNotNull();
    }
}
