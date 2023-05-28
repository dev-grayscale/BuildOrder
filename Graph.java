import java.util.*;

/**
 * This will be the structure of the graph to have an intuitive interface to work with.
 *
 * Info: For the goal of simplicity, we chose not to add setters and getters.
 *
 * To begin with, we will have a method to populate the graph: <b>setProjects</b>. We made it, so it could be reused to re-populate the same Graph instance
 * with projects. The 1st phase of the method builds a graph with all the projects without dependencies. In the second part we set the dependencies.
 * It also uses an <b>initialProjects</b> data structure to filter out those that have none at all.
 */
public class Graph {
  private final Map<String, Project> projects = new HashMap<>();

  // Those with no dependencies
  private final Map<String, Project> initialProjects = new HashMap<>();

  public void setProjects(List<String> projectsIds, List<String[]> dependencies) {
    projects.clear();
    initialProjects.clear();

    // add all the projects in the graph with
    // no dependencies
    for (String projectId : projectsIds) {
      Project project = new Project(projectId);

      addProject(projects, project);
      addProject(initialProjects, project);
    }

    // Populate dependencies
    for (String[] pair : dependencies) {
      Project project = getProject(pair[1]);
      Project dependency = getProject(pair[0]);

      if (project == null || dependency == null) {
        throw new IllegalArgumentException(String.format("Project %s or/and %s does not exist", pair[0], pair[1]));
      }

      // Remove all projects with more than 1
      // dependency as we build the graph, which
      // will save additional traversals later
      initialProjects.remove(project.id);

      project.addDependency(dependency);
    }
  }

  public void addProject(Map<String, Project> collection, Project project) {
    if (project == null || project.id == null) {
      throw new IllegalArgumentException("Invalid project");
    }

    collection.putIfAbsent(project.id, project);
  }

  public Project getProject(String id) {
    return projects.get(id);
  }

  /**
   * The high overview of the functionality is to:
   *
   * 1. Create a graph and populate the dependencies
   * 2. Start the build from the projects with no dependencies. If there are none (when projects > 0) - there's no valid build order.
   * 3. As we build the projects with no dependencies we also need to remove them from the projects that depend upon them. Once done, if the dependent ones
   * have no more dependencies - we add them to the queue to proceed with. We repeat the process until all are build.
   * If, at some point, we cannot proceed further with the build, as there are still projects to build and all of them
   * have dependencies > 0 - there's no valid build order.
   *
   * That being said, there are few places to note:
   *
   * 1. initialProjects is a pre-computed (during initial build) to save us an additional graph iteration. (in case the additional memory is not an issue,
   * it improves Time Complexity)
   * 2. Using a Queue, we keep the projects with no dependencies to proceed with, and adding the ones which were freed of them recently.
   * 3. If at some point we establish that the head of the queue has a Project with dependencies > 0 -> we terminate as no build order exist.
   */
  public List<Project> buildOrder() {
    List<Project> buildOrder = new ArrayList<>(projects.size());
    Queue<Project> queue = new LinkedList<>(initialProjects.values());

    while (!queue.isEmpty() && queue.peek().dependencies == 0) {
      Project project = queue.remove();

      buildOrder.add(project);

      // Remove as dependency from parents
      for (Project parent : project.parents.values()) {
        if (--parent.dependencies == 0) {
          queue.add(parent);
        }
      }
    }

    if (buildOrder.size() != projects.size()) {
      throw new IllegalArgumentException("No valid build order");
    }

    return buildOrder;
  }
}
