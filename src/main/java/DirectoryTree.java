import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DirectoryNode {

    String name;
    Map<String, DirectoryNode> children;
    String blob;

    public DirectoryNode(String name) {
        this.name = name;
        this.children = new HashMap<>();
        this.blob = null;
    }
}

public class DirectoryTree {
    private DirectoryNode root;

    public DirectoryTree() {
        root = new DirectoryNode("ecm");
    }

    public static void main(String[] args) {
        DirectoryTree directoryTree = new DirectoryTree();

        // Case 1: Adding a new file
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Demo.yml", "Content of file.json");

        // Visualize the directory tree
        System.out.println("Adding a new file\n");
        directoryTree.visualize();

        // Case 2: Adding a new file inside a directory
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir1/temp.yml", "Content of file.json");

        // Visualize the directory tree
        System.out.println("Adding a new file inside a directory\n");
        directoryTree.visualize();
        // Getting contents of a file using the key
        String content = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1/temp.yml");
        System.out.println("Contents of the file: " + content);

        // Case 3: Adding a file which is already present -- It'll override the existing file
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir1/temp.yml", "Content of overridden file.json");

        // Visualize the directory tree
        System.out.println("Adding a file which is already present -- It'll override the existing file\n");
        directoryTree.visualize();
        // Getting contents of a file using the key
        String contentNew = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1/temp.yml");
        System.out.println("Contents of the overridden file: " + contentNew);

        // Case 4: Adding another file in already present directory
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir1/temp2.yml", "Content of file2.json");

        // Visualize the directory tree
        System.out.println("Adding another file in already present directory\n");
        directoryTree.visualize();

        // Case 5: Adding another directory inside same env
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir2/test.yml", "Content of file2.json");

        // Visualize the directory tree
        System.out.println("Adding another directory inside same env\n");
        directoryTree.visualize();

        // Case 5: Adding duplicate file in the new directory under same env
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir2/temp2.yml", "Content of file2.json");

        // Visualize the directory tree
        System.out.println("Adding duplicate file in the new directory under same env\n");
        directoryTree.visualize();
    }

    // Put method to insert a node("/v1/kv/ecm/AIM600000712/Repo1/e1/F1/temp.yml")
    public void put(String key, String blob) {
        // Splitting the key
        String[] path = key.split("/");

        DirectoryNode current = root;

        for (int i = 1; i < path.length - 1; i++) {
            if (!current.children.containsKey(path[i])) {
                DirectoryNode newDir = new DirectoryNode(path[i]);
                current.children.put(path[i], newDir);
                current = newDir;
            } else {
                current = current.children.get(path[i]);
            }
        }

        String fileName = path[path.length - 1];

        // If file is not present
        if (!current.children.containsKey(fileName)) {
            DirectoryNode newFile = new DirectoryNode(fileName);
            newFile.blob = blob;
            current.children.put(fileName, newFile);
        } else {
            DirectoryNode existingFile = current.children.get(fileName);
            existingFile.blob = blob;
        }
    }

    // Helper function to retrieve the content of a file with a given key
    public String get(String key) {
        String[] path = key.split("/");

        DirectoryNode current = root;

        for (int i = 1; i < path.length; i++) {
            if (!current.children.containsKey(path[i])) {
                // If any part of the path is missing, return null (file not found)
                return null;
            }
            current = current.children.get(path[i]);
        }

        // If the last node represents a file and not a directory, return its content
        return current.blob;
    }

    // Helper function to visualize the directory tree
    private void visualizeDirectoryTree(DirectoryNode node, String indent) {
        System.out.println(indent + "- " + node.name);
        for (DirectoryNode child : node.children.values()) {
            visualizeDirectoryTree(child, indent + "  ");
        }
    }

    public void visualize() {
        System.out.println("Directory Tree Visualization:");
        visualizeDirectoryTree(root, "");
    }

    public List<String> get(String key, boolean recurse) {
        String[] path = key.split("/");
        DirectoryNode current = root;

        // Traverse the directory tree to find the specified key
        for (int i = 1; i < path.length; i++) {
            if (!current.children.containsKey(path[i])) {
                // If any part of the path is missing, return an empty list
                return new ArrayList<>();
            }
            current = current.children.get(path[i]);
        }

        // If the specified key represents a file (not a directory), return its content
        if (current.blob != null) {
            List<String> result = new ArrayList<>();
            result.add(current.blob);
            return result;
        }

        // If recurse flag is true, fetch contents of all files within the directory and its subdirectories
        if (recurse) {
            List<String> result = new ArrayList<>();
            fetchFileContents(current, result);
            return result;
        }

        // If the specified key represents a directory and recurse flag is false, return an empty list
        return new ArrayList<>();
    }

    // Helper method to fetch contents of all files within a directory and its subdirectories (recursive)
    private void fetchFileContents(DirectoryNode directory, List<String> result) {
        for (DirectoryNode child : directory.children.values()) {
            if (child.blob != null) {
                result.add(child.blob);
            } else {
                fetchFileContents(child, result);
            }
        }
    }
}




