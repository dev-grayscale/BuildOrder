import java.util.List;

/**
 * Given a list of projects and dependencies that need to be built, determine a valid order in which to build them. Return an error, if such cannot be found.
 *
 * Example
 *
 * Input:
 *   projects: a, b, c, d, e, f
 *   dependencies: (a, d), (f, b), (b, d), (f, a), (d, c)
 *
 * Output: f, e, a, b, d, c
 *
 * (Challenge definition seen in Cracking the coding interview book)
 */
public class Main {
  /**
   * Following the requirements above, we need to create a graph using the passed data and attempt to find a build order.
   */
  public static List<Project> buildOrder(List<String> projects, List<String[]> dependencies) {
    if (projects == null || projects.isEmpty()) {
      throw new IllegalArgumentException("No projects provided");
    }

    Graph graph = new Graph();
    graph.setProjects(projects, dependencies);

    return graph.buildOrder();
  }
}
