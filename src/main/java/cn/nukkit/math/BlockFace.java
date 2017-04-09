package cn.nukkit.math;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public enum BlockFace {
    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vector3(0, -1, 0)),
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vector3(0, 1, 0)),
    NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vector3(0, 0, -1)),
    SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vector3(0, 0, 1)),
    WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vector3(-1, 0, 0)),
    EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vector3(1, 0, 0));

    /**
     * Ordering index for D-U-N-S-W-E
     */
    private final int index;

    /**
     * Index of the opposite Facing in the VALUES array
     */
    private final int opposite;

    /**
     * Ordering index for the HORIZONTALS field (S-W-N-E)
     */
    private final int horizontalIndex;
    private final String name;
    private final Axis axis;
    private final AxisDirection axisDirection;

    /**
     * Normalized Vector that points in the direction of this Facing
     */
    private final Vector3 directionVec;

    /**
     * All facings in D-U-N-S-W-E order
     */
    private static final BlockFace[] VALUES = new BlockFace[6];

    /**
     * All Facings with horizontal axis in order S-W-N-E
     */
    private static final BlockFace[] HORIZONTALS = new BlockFace[4];
    private static final Map<String, BlockFace> NAME_LOOKUP = new HashMap<>();

    BlockFace(int index, int opposite, int horizontalIndex, String name, AxisDirection axisDirection, Axis axis, Vector3 directionVec) {
        this.index = index;
        this.horizontalIndex = horizontalIndex;
        this.opposite = opposite;
        this.name = name;
        this.axis = axis;
        this.axisDirection = axisDirection;
        this.directionVec = directionVec;
    }

    /**
     * Get the Index of this Facing (0-5). The order is D-U-N-S-W-E
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Get the index of this horizontal facing (0-3). The order is S-W-N-E
     */
    public int getHorizontalIndex() {
        return this.horizontalIndex;
    }

    /**
     * Get the AxisDirection of this Facing.
     */
    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    /**
     * Get the opposite Facing (e.g. DOWN => UP)
     */
    public BlockFace getOpposite() {
        return getFront(this.opposite);
    }

    /**
     * Rotate this Facing around the Y axis clockwise (NORTH => EAST => SOUTH => WEST => NORTH)
     */
    public BlockFace rotateY() {
        switch (this) {
            case NORTH:
                return EAST;

            case EAST:
                return SOUTH;

            case SOUTH:
                return WEST;

            case WEST:
                return NORTH;

            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        }
    }

    /**
     * Rotate this Facing around the Y axis counter-clockwise (NORTH => WEST => SOUTH => EAST => NORTH)
     */
    public BlockFace rotateYCCW() {
        switch (this) {
            case NORTH:
                return WEST;

            case EAST:
                return NORTH;

            case SOUTH:
                return EAST;

            case WEST:
                return SOUTH;

            default:
                throw new IllegalStateException("Unable to get CCW facing of " + this);
        }
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetX() {
        return this.axis == Axis.X ? this.axisDirection.getOffset() : 0;
    }

    public int getFrontOffsetY() {
        return this.axis == Axis.Y ? this.axisDirection.getOffset() : 0;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetZ() {
        return this.axis == Axis.Z ? this.axisDirection.getOffset() : 0;
    }

    /**
     * Same as getName, but does not override the method from Enum.
     */
    public String getName2() {
        return this.name;
    }

    public Axis getAxis() {
        return this.axis;
    }

    /**
     * Get a Facing by it's index (0-5). The order is D-U-N-S-W-E. Named getFront for legacy reasons.
     */
    public static BlockFace getFront(int index) {
        return VALUES[MathHelper.abs(index % VALUES.length)];
    }

    /**
     * Get a Facing by it's horizontal index (0-3). The order is S-W-N-E.
     */
    public static BlockFace getHorizontal(int index) {
        return HORIZONTALS[MathHelper.abs(index % HORIZONTALS.length)];
    }

    /**
     * Get the Facing corresponding to the given angle (0-360). An angle of 0 is SOUTH, an angle of 90 would be WEST.
     */
    public static BlockFace fromAngle(double angle) {
        return getHorizontal(NukkitMath.floorDouble(angle / 90.0D + 0.5D) & 3);
    }

    public float getHorizontalAngle() {
        return (float) ((this.horizontalIndex & 3) * 90);
    }

    /**
     * Choose a random Facing using the given Random
     */
    public static BlockFace random(Random rand) {
        return values()[rand.nextInt(values().length)];
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public static BlockFace getFacingFromAxis(AxisDirection axisDirection, Axis axis) {
        for (BlockFace face : values()) {
            if (face.getAxisDirection() == axisDirection && face.getAxis() == axis) {
                return face;
            }
        }

        throw new IllegalArgumentException("No such direction: " + axisDirection + " " + axis);
    }

    static {
        for (BlockFace enumfacing : values()) {
            VALUES[enumfacing.index] = enumfacing;

            if (enumfacing.getAxis().isHorizontal()) {
                HORIZONTALS[enumfacing.horizontalIndex] = enumfacing;
            }

            NAME_LOOKUP.put(enumfacing.getName2().toLowerCase(), enumfacing);
        }
    }

    public enum Axis implements Predicate<BlockFace> {
        X("x", Plane.HORIZONTAL),
        Y("y", Plane.VERTICAL),
        Z("z", Plane.HORIZONTAL);

        private static final Map<String, Axis> NAME_LOOKUP = new HashMap<>();
        private final String name;
        private final Plane plane;

        Axis(String name, Plane plane) {
            this.name = name;
            this.plane = plane;
        }

        public String getName2() {
            return this.name;
        }

        public boolean isVertical() {
            return this.plane == Plane.VERTICAL;
        }

        public boolean isHorizontal() {
            return this.plane == Plane.HORIZONTAL;
        }

        public String toString() {
            return this.name;
        }

        public boolean apply(BlockFace p_apply_1_) {
            return p_apply_1_ != null && p_apply_1_.getAxis() == this;
        }

        public Plane getPlane() {
            return this.plane;
        }

        public String getName() {
            return this.name;
        }

        static {
            for (Axis enumfacing$axis : values()) {
                NAME_LOOKUP.put(enumfacing$axis.getName2().toLowerCase(), enumfacing$axis);
            }
        }
    }

    public enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        AxisDirection(int offset, String description) {
            this.offset = offset;
            this.description = description;
        }

        public int getOffset() {
            return this.offset;
        }

        public String toString() {
            return this.description;
        }
    }

    public enum Plane implements Predicate<BlockFace>, Iterable<BlockFace> {
        HORIZONTAL,
        VERTICAL;

        public BlockFace[] facings() {
            switch (this) {
                case HORIZONTAL:
                    return new BlockFace[]{NORTH, EAST, SOUTH, WEST};
                case VERTICAL:
                    return new BlockFace[]{UP, DOWN};
                default:
                    throw new Error("Someone\'s been tampering with the universe!");
            }
        }

        public BlockFace random(NukkitRandom rand) {
            BlockFace[] faces = this.facings();
            return faces[rand.nextBoundedInt(faces.length)];
        }

        public boolean apply(BlockFace face) {
            return face != null && face.getAxis().getPlane() == this;
        }

        public Iterator<BlockFace> iterator() {
            return Iterators.forArray(this.facings());
        }
    }
}
