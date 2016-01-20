package suite;

import test.business.*;
import test.db.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ BasicServiceTest.class, DataTest.class, DBFactoryTest.class, RMIServiceTest.class })
public class AllTests {

}
