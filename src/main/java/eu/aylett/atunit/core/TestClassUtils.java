package eu.aylett.atunit.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import eu.aylett.atunit.*;
import eu.aylett.atunit.easymock.EasyMockFramework;
import eu.aylett.atunit.guice.GuiceContainer;
import eu.aylett.atunit.jmock.JMockFramework;
import eu.aylett.atunit.mockito.MockitoFramework;
import eu.aylett.atunit.spring.SpringContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

final public class TestClassUtils {

	/**
	 * Gets the old-style MockFramework class for the given test class by checking for a MockFramework or MockFrameworkClass annotation.
	 * 
	 * @throws IncompatibleAnnotationException if both MockFramework and MockFrameworkClass are present.
	 */
	public static Class<? extends MockFramework> getMockFrameworkClass(Class<?> testClass) throws IncompatibleAnnotationException {
		Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;
		
		eu.aylett.atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(eu.aylett.atunit.MockFramework.class);
		MockFrameworkClass mockFrameworkClassAnno = testClass.getAnnotation(MockFrameworkClass.class);
		
		if ( mockFrameworkAnno != null && mockFrameworkClassAnno != null )
			throw new IncompatibleAnnotationException(eu.aylett.atunit.MockFramework.class, MockFrameworkClass.class);

		if ( mockFrameworkAnno != null ) {
			switch ( mockFrameworkAnno.value() ) {
				case EASYMOCK: return EasyMockFramework.class;
				case JMOCK: return JMockFramework.class;
				case MOCKITO: return MockitoFramework.class;
				default:
					throw new IllegalStateException("Expected switch block to be exhaustive:" + mockFrameworkAnno.value());
			}
		}
		
		if ( mockFrameworkClassAnno != null ) {
			mockFrameworkClass = mockFrameworkClassAnno.value();
		}
		
		return mockFrameworkClass;
	}
	
	/**
	 * Gets all fields (declared and inherited) on the given class.
	 */
	public static Set<Field> getFields(Class<?> testClass) {
		Set<Field> fields = Sets.newHashSet(testClass.getDeclaredFields());
		Class<?> c = testClass;
		while ( (c = c.getSuperclass()) != null ) {
			for ( Field f : c.getDeclaredFields() ) {
				if ( !Modifier.isStatic(f.getModifiers())
					 && !Modifier.isPrivate(f.getModifiers())
				     ) {
					fields.add(f);
				}
			}
		}
		return ImmutableSet.copyOf(fields);
	}
	
	/**
	 * Gets the one and only field in the set annotated with Unit.
	 * @throws NoUnitException if no field is annotated with Unit.
	 * @throws TooManyUnitsException if more than one field is annotated with Unit.
	 */
	public static Field getUnitField(Set<Field> fields)  throws NoUnitException, TooManyUnitsException {
		Field unitField = null;
		for ( Field field : fields ) {
			for ( Annotation anno : field.getAnnotations() ) {
				if ( Unit.class.isAssignableFrom(anno.annotationType())) {
					if ( unitField != null ) throw new TooManyUnitsException("Already had field " + unitField + " when I found field " + field);
					unitField = field;
				}
			}
		}
		if ( unitField == null ) throw new NoUnitException();
		return unitField;
	}
	
	/**
	 * Gets the old-style Container class for the given test class by looking for a Container or ContainerClass annotation.
	 * 
	 * @throws IncompatibleAnnotationException if both Container and ContainerClass annotations are present.
	 */
	public static Class<? extends Container> getContainerClass(Class<?> testClass) throws IncompatibleAnnotationException {
		
		eu.aylett.atunit.Container containerAnno = testClass.getAnnotation(eu.aylett.atunit.Container.class);
		ContainerClass containerClassAnno = testClass.getAnnotation(ContainerClass.class);
		
		if ( containerAnno != null && containerClassAnno != null )
			throw new IncompatibleAnnotationException(eu.aylett.atunit.Container.class, ContainerClass.class);

		if ( containerAnno != null ) {
			switch ( containerAnno.value() ) {
				case GUICE: return GuiceContainer.class;
				case SPRING: return SpringContainer.class;
			}
		}
		
		if ( containerClassAnno != null ) {
			return containerClassAnno.value();
		}
		
		return NoContainer.class;
	}
	
	
	/**
	 * Gets all fields annotated with Mock.
	 */
	public static Set<Field> getMockFields(Set<Field> fields) {
		Set<Field> mocks = Sets.newHashSet();
		for ( Field field : fields ) {
			if ( field.getAnnotation(Mock.class) != null ) {
				mocks.add(field);
			}
		}
		return ImmutableSet.copyOf(mocks);
	}
	
	/**
	 * Gets all fields annotated with Stub
	 */
	public static Set<Field> getStubFields(Set<Field> fields) {
		Set<Field> stubs = Sets.newHashSet();
		for ( Field field : fields ) {
			if ( field.getAnnotation(Stub.class) != null ) {
				stubs.add(field);
			}
		}
		return ImmutableSet.copyOf(stubs);
	}
	
}
