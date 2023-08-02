/*
package ClassImplementation;

public class DirectoryTree {
    private DirectoryNode root;

    public DirectoryTree() {
        root = new DirectoryNode();
    }

    public void put(String key, String blob) {
        String[] path = key.split("/");
        DirectoryNode current = root;

        for (String level : path) {
            if (!current.getChildren().containsKey(level)) {
                current.getChildren().put(level, new DirectoryNode());
            }
            current = current.getChildren().get(level);
        }

        current.setContent(blob);
    }

    public String get(String key) {
        String[] path = key.split("/");
        DirectoryNode current = root;

        for (String level : path) {
            current = current.getChildren().get(level);
            if (current == null) {
                return null;
            }
        }

        return current.getContent();
    }

    public static void main(String[] args) {
        DirectoryTree tree = new DirectoryTree();
        tree.put("ecm/AIM600000712/Repo1/e1/Dir1/file1.json", "Content of file1.json");
        tree.put("ecm/AIM600000712/Repo1/e1/Dir1/file2.json", "Content of file2.json");

        String content1 = tree.get("ecm/AIM600000712/Repo1/e1/Dir1/file1.json");
        String content2 = tree.get("ecm/AIM600000712/Repo1/e1/Dir1/file2.json");

        System.out.println("Content of file1.json: " + content1);
        System.out.println("Content of file2.json: " + content2);
    }
}*/
