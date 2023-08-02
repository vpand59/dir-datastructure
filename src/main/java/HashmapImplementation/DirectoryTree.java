package HashmapImplementation;

import java.util.ArrayList;
import java.util.List;

public class DirectoryTree {
    private DirectoryNode root;

    public DirectoryTree() {
        root = new DirectoryNode("ecm");
    }

    // Put method to insert a node("ecm/AIM600000712/Repo1/e1/F1/temp.yml")
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

    // Function to retrieve the content of a file with a given key
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

    // Function to visualize the directory tree
    private void visualizeDirectoryTree(DirectoryNode node, String indent) {
        System.out.println(indent + "- " + node.name);
        for (DirectoryNode child : node.children.values()) {
            visualizeDirectoryTree(child, indent + "  ");
        }
    }

    public void visualize() {
        System.out.println("Directory Tree:");
        visualizeDirectoryTree(root, "");
    }

    // key will be like ecm/AIM600000712/Repo1/e1/F1
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
            fetchAllFiles(current, result);
            return result;
        }

        // If the specified key represents a directory and recurse flag is false, return an empty list
        return new ArrayList<>();
    }

    // Helper method to fetch contents of all files within a directory and its subdirectories (recursive)
    private void fetchAllFiles(DirectoryNode directory, List<String> result) {
        for (DirectoryNode child : directory.children.values()) {
            if (child.blob != null) {
                result.add(child.blob);
            } else {
                fetchAllFiles(child, result);
            }
        }
    }

    public static void main(String[] args) {
        DirectoryTree directoryTree = new DirectoryTree();

        // Case 1.1: Adding a new file
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Demo.yml", "Content of Demo.yml");

        // Visualize the directory tree
        System.out.println("Adding a new file:");
        directoryTree.visualize();

        // Case 1.2: Adding a new file inside a directory
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir1/Temp.yml", "Content of Temp.yml");

        // Visualize the directory tree
        System.out.println("\nAdding a new file inside a directory:");
        directoryTree.visualize();
        // Getting contents of a file using the key
        String content = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1/Temp.yml");
        System.out.println("Contents of the file Temp.yml:--------> " + content);

        // Case 1.3: Adding a file which is already present -- It'll override the existing file
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir1/Temp.yml", "Content of overridden Temp.yml");

        // Visualize the directory tree
        System.out.println("\nAdding a file which is already present -- It'll override the existing file:");
        directoryTree.visualize();
        // Getting contents of a file using the key
        String contentNew = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1/Temp.yml");
        System.out.println("Contents of file temp.yml:--------> " + contentNew);

        // Case 1.4: Adding another file in already present directory
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir1/Temp2.yml", "Content of Temp2.yml");

        // Visualize the directory tree
        System.out.println("\nAdding another file in already present directory:");
        directoryTree.visualize();

        // Case 1.5: Adding another directory inside same env
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir2/Test.yml", "Content of Test.yml");

        // Visualize the directory tree
        System.out.println("\nAdding another directory inside same env:");
        directoryTree.visualize();

        // Case 1.6: Adding a file in the new directory under same env with same name as a file already present in a different directory
        directoryTree.put("ecm/AIM600000712/Repo1/e1/Dir2/Temp2.yml", "Content of Temp2.yml inside Dir2");

        // Visualize the directory tree
        System.out.println("\nAdding a file in the new directory under same env with same name as a file already present in a different directory:");
        directoryTree.visualize();


        // Getting contents of a file using the key
        String content1 = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1/Temp2.yml");
        System.out.println("Contents of file temp2.yml present under Dir1 directory:--------> " + content1);
        String content2 = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir2/Temp2.yml");
        System.out.println("Contents of file temp2.yml present under Dir2 directory:--------> " + content2);
        System.out.println("\n\n");

        // Case 2.1 Testing the get(String key, boolean recurse) method
        List<String> files = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1", true);
        System.out.println("\nCase 2.1 : Fetching everything inside Dir1");
        for (String file : files) {
            System.out.println(file);
        }

        // Case 2.2 Testing the get(String key, boolean recurse) method
        List<String> files2 = directoryTree.get("ecm/AIM600000712/Repo1/e1/Dir1/Temp.yml", true);
        System.out.println("\nCase 2.2 : Giving recurse true, but key contains a file");
        for (String file : files2) {
            System.out.println(file);
        }

        // Case 2.3 Testing the get(String key, boolean recurse) method
        List<String> files3 = directoryTree.get("ecm/AIM600000712/Repo1/e1", true);
        System.out.println("\nCase 2.3 : Fetching everything under e1");
        for (String file : files3) {
            System.out.println(file);
        }


    }


}




