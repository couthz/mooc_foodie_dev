import com.zhc.Application;
import com.zhc.controller.ItemsController;
import com.zhc.service.StuService;
import com.zhc.service.TestTransService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TranTest {

    @Autowired
    private StuService stuService;

    @Autowired
    private TestTransService testTransService;

    @Autowired
    private ItemsController itemsController;

    @Test
    public void myTest() {
        itemsController.comments("cook-1001", null, 1,10);
    }
}
