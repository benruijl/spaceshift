package engine;

import java.util.HashMap;
import java.util.Map;

/** Resource manager singleton. */
public enum ResourceManager {
    INSTANCE;

    private final Map<Object, Shader> shaders;

    ResourceManager() {
        shaders = new HashMap<Object, Shader>();
    }

    public Shader getShader(Object id) {
        return shaders.get(id);
    }
    
    public void addShader(Object id, Shader shader) {
        shaders.put(id, shader);
    }
}
