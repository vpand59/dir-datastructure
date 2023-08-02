package HashmapImplementation;

import java.util.HashMap;
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
