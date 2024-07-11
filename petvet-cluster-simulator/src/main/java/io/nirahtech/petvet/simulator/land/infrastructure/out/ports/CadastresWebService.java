package io.nirahtech.petvet.simulator.land.infrastructure.out.ports;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface CadastresWebService {
    Set<String> listAllDepartments() throws IOException;
    Set<String> listAllZipCodesByDepartment(final String department) throws IOException;
    Map<String, Object> getCadastreByDepartmentAndZipCode(final String department, final String zipCode) throws IOException;
}
