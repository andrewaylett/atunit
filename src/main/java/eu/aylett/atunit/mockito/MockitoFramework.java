package eu.aylett.atunit.mockito;

import eu.aylett.atunit.Mock;
import eu.aylett.atunit.Stub;
import eu.aylett.atunit.core.MockFramework;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Mocks objects using Mockito
 */
public class MockitoFramework implements MockFramework {
    @Override
    public Map<Field, Object> getValues(Field[] fields) throws Exception {
        Map<Field,Object> mocksAndStubs = new HashMap<>();

        for ( Field field : fields ) {
            if ( field.getAnnotation(Mock.class) != null ) {
                mocksAndStubs.put(field, Mockito.mock(field.getType()));
            } else if ( field.getAnnotation(Stub.class) != null ) {
                mocksAndStubs.put(field, Mockito.mock(field.getType()));
            }
        }

        return mocksAndStubs;
    }
}
