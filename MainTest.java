import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class MainTest {

  @Test
  public void test() {
    List<String> projects;
    List<String[]> dependencies;
    List<Project> buildOrder;

    Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.buildOrder(null, Collections.emptyList()));
    Assertions.assertEquals("No projects provided", t.getMessage());

    t = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.buildOrder(Collections.emptyList(), Collections.emptyList()));
    Assertions.assertEquals("No projects provided", t.getMessage());

    projects = Arrays.asList("a", "b", "c", "d", "e", "f");
    dependencies = Collections.emptyList();

    buildOrder = Main.buildOrder(projects, dependencies);
    Assertions.assertTrue(isValid(buildOrder));

    projects = Arrays.asList("a", "b", "c", "d", "e", "f");
    dependencies = Arrays.asList(
      new String[] { "a", "d" },
      new String[] { "f", "b" },
      new String[] { "b", "d" },
      new String[] { "f", "a" },
      new String[] { "d", "c" }
    );

    buildOrder = Main.buildOrder(projects, dependencies);
    Assertions.assertEquals(6, buildOrder.size());
    Assertions.assertTrue(isValid(buildOrder));

    projects = Arrays.asList("a", "b", "c", "d", "e", "f");
    dependencies = Arrays.asList(
      new String[] { "a", "d" },
      new String[] { "f", "b" },
      new String[] { "b", "d" },
      new String[] { "f", "a" },
      new String[] { "d", "c" },
      new String[] { "e", "f" },
      new String[] { "a", "e" }
    );

    List<String> finalProjects = projects;
    List<String[]> finalDependencies = dependencies;
    t = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.buildOrder(finalProjects, finalDependencies));
    Assertions.assertEquals("No valid build order", t.getMessage());

    projects = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
    dependencies = Arrays.asList(
      new String[] { "a", "d" },
      new String[] { "f", "b" },
      new String[] { "b", "d" },
      new String[] { "f", "a" },
      new String[] { "d", "c" },
      new String[] { "e", "f" },
      new String[] { "a", "e" }
    );

    List<String> finalProjects1 = projects;
    List<String[]> finalDependencies1 = dependencies;
    t = Assertions.assertThrows(IllegalArgumentException.class, () -> Main.buildOrder(finalProjects1, finalDependencies1));
    Assertions.assertEquals("No valid build order", t.getMessage());
  }

  /**
   * The buildOrder must be sorted from the least dependency count to highest.
   */
  private boolean isValid(List<Project> buildOrder) {
    for (Project project : buildOrder) {
      // To be considered valid, all the dependencies
      // of each subsequent project should be already built or have none at all.
      if (project.dependencies > 0) {
        return false;
      }
    }

    return true;
  }
}
