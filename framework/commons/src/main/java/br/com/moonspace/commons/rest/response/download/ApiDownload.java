package br.com.moonspace.commons.rest.response.download;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiDownload extends ResponseEntity<byte[]> {

    protected static Builder newInstance() {
        return new Builder();
    }

    protected ApiDownload(byte[] body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public static ApiDownload of(byte[] content, String fileName, HttpStatus httpStatus) {
        return newInstance()//
                .withContent(content)//
                .withFileName(fileName)//
                .withHttpStatus(httpStatus)//
                .response();
    }

    public static ApiDownload of(byte[] content, String fileName) {
        return newInstance()//
                .withContent(content)//
                .withFileName(fileName)//
                .withHttpStatus(HttpStatus.OK)//
                .response();
    }

    protected static class Builder {

        private String fileName;
        private byte[] content;
        private HttpStatus httpStatus;

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withContent(byte[] content) {
            this.content = content;
            return this;
        }

        public Builder withHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ApiDownload response() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(this.content.length);
            headers.setContentDisposition(ContentDisposition.parse("attachment; filename=" + this.fileName));
            return new ApiDownload(this.content, headers, httpStatus == null ? HttpStatus.OK : httpStatus);
        }

    }

}