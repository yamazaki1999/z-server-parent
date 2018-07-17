package z.server.workflow;

import java.util.Objects;

/**
 * @description:
 * @author: zbn
 * @create: 2018-06-04 21:58
 **/
public class TestR {
    private String id;
    private String name;

    public TestR(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public TestR() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestR{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        TestR t = (TestR) o;
        return !(this.hashCode() == t.hashCode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
