import java.util.*;

public class S_TraversalPath{

    List<Pixel> PathTaken;
    List<S_TraversalPath> Children;
    public double TimeTaken;

    public S_TraversalPath(List<Pixel> pathTaken, List<S_TraversalPath> children, double timeTaken) {
        this.PathTaken = pathTaken;
        this.Children = children;
        this.TimeTaken = timeTaken;
    }

    public S_TraversalPath(List<Pixel> pathTaken, double timeTaken) {
        this(pathTaken, new ArrayList<>(), timeTaken);
    }

    public S_TraversalPath(S_TraversalPath copy) {
        this.PathTaken = new ArrayList<>();
        this.PathTaken.addAll(copy.PathTaken);
        this.Children = new ArrayList<>();
        this.TimeTaken = copy.TimeTaken;
    }

    public List<Pixel> getPathTaken() {
        return PathTaken;
    }

    public List<S_TraversalPath> getChildren() {
        return Children;
    }

    public Pixel getLastNode() {
        return this.PathTaken.get(this.PathTaken.size() - 1);
    }

    public double getTimeTaken() {
        return TimeTaken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        S_TraversalPath that = (S_TraversalPath) o;
        if (Objects.equals(PathTaken, that.PathTaken)) {
            return true;
        }

        for (int i = 0; i < PathTaken.size(); i++) {
            if (PathTaken.get(i) != that.PathTaken.get(i))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PathTaken);
    }

    @Override
    public String toString() {
        return "end=" + getLastNode().toString();
    }
}