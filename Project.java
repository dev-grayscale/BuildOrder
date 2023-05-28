import java.util.HashMap;
import java.util.Map;

/**
 * The structure of the project contains:
 *
 * 1. id which is a simple String (to simplify the complexity of the challenge)
 * 2. dependencies - to keep track of the amount of projects the current one depends on
 * 3. parents - to act like reverse-index and have immediate access to the projects that depend on the current one, instead of traversing the graph and looking
 * for them to decrease their dependencies count.
 * 4. When a dependency is added to a project
 *  - increment dependencies count of that/current project (the dependency have to be resolved first before the current one)
 *  - add the current project within the parents of the dependency project
 */
public class Project {
  public String id;

  public Map<String, Project> parents = new HashMap<>();

  public int dependencies = 0;

  public Project(String id) {
    this.id = id;
  }

  public void addDependency(Project project) {
    dependencies++;

    project.parents.put(this.id, this);
  }
}
