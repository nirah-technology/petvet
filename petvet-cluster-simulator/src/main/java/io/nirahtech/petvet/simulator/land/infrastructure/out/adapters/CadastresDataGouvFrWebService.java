package io.nirahtech.petvet.simulator.land.infrastructure.out.adapters;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.nirahtech.petvet.simulator.land.infrastructure.out.ports.CadastresWebService;

public class CadastresDataGouvFrWebService implements CadastresWebService {

    private final URI uri;

    final HttpClient httpClient;

    public CadastresDataGouvFrWebService()  {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("cadastres");
        this.uri = URI.create(resourceBundle.getString("cadastres.url"));
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public Set<String> listAllDepartments()  throws IOException{
        final HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(this.uri).build();
        final HttpResponse<Stream<String>> httpResponse;
        try {
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofLines());
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
        final String regex = ">(\\d*)/<";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        return httpResponse.body().map(line ->  {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;

        }).filter((department -> Objects.nonNull(department)))
        .collect(Collectors.toSet());
    }

    @Override
    public Set<String> listAllZipCodesByDepartment(String department)  throws IOException{
        final URI uriToRequest = URI.create(this.uri.toString().concat(String.format("/%s/", department)));
        final HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(uriToRequest).build();
        final HttpResponse<Stream<String>> httpResponse;
        try {
            httpResponse = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofLines());
        } catch (IOException | InterruptedException e) {
            throw new IOException(e);
        }
        final String regex = ">(\\d*)/<";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        return httpResponse.body().map(line ->  {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
            return null;

        }).filter((zipCode -> Objects.nonNull(zipCode)))
        .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> getCadastreByDepartmentAndZipCode(String department, String zipCode)  throws IOException{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCadastreByDepartmentAndZipCode'");
    }
    
}
