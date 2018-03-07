package com.github.ivmikhail.fx.vtb.dto;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DtoObjectsTest {
    private static final int EXPECTED_CLASS_COUNT = 1;

    // The top level package for all classes to be tested
    private String packageName = this.getClass().getPackage().getName();
    private List<PojoClass> pojoClasses;

    @Before
    public void setUp() {
        // Get all classes recursively under package
        pojoClasses = PojoClassFactory.getPojoClassesRecursively(packageName, pojoClass -> {
            //exclude this class because of it's not pojo, it's a test
            return !this.getClass().getName().equals(pojoClass.getName());
        });
    }

    @Test
    public void testGettersAndSettersOfAllDtoObjects() {
        assertEquals(EXPECTED_CLASS_COUNT, 3);

        Validator v = ValidatorBuilder.create()
                // Lets make sure that we have a getter and a setter for every field defined.
                .with(new SetterMustExistRule())
                .with(new GetterMustExistRule())

                // Lets also validate that they are behaving as expected
                .with(new SetterTester())
                .with(new GetterTester())

                .build();

        v.validate(pojoClasses); //start the Test
    }
}