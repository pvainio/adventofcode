package aoc2021;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Advent of Code (AOC) 2021 Day 23 Amphipod
 */
public class D23 {

    record State(List<List<String>> rooms, List<String> hallway) {
        int roomSize() {
            return amphipods().mapToInt(a -> a.charAt(1) - '0').max().getAsInt();
        }
        Stream<String> amphipodsInRooms() {
            return rooms.stream().flatMap(r -> r.stream()).filter(a -> a.length() > 1);
        }
        Stream<String> amphipods() {
            return Stream.concat(hallway.stream().filter(a -> a.length() > 1), amphipodsInRooms());
        }
    }

    record Move(State state, int energy){}

    public static void main(String... args) throws IOException {
        State state1 = new State(List.of(List.of("D1","C1"), List.of("C2", "A1"), List.of("D2","A2"), List.of("B1","B2")), Collections.nCopies(11, "."));
        State state2 = new State(List.of(List.of("D1","D3","D4","C1"), List.of("C2","C3","B3", "A1"), List.of("D2","B4","A3","A2"), List.of("B1","A4","C4","B2")), Collections.nCopies(11, "."));
        
        int part1 = solve(state1);
        System.out.printf("part 1: %s\n", part1);

        int part2 = solve(state2); // will take some time
        System.out.printf("part 2: %s\n", part2);
    }

    static int solve(State state) {
        Map<State,Integer> energy = new ConcurrentHashMap<>(Map.of(state, 0));  // States and lowest energy to achieve the state
        int min = Integer.MAX_VALUE;
        for (int updated = 0; energy.size() > updated;) { //while no more updates
            updated = energy.size();
            // get all the possible moves from current states forward
            var possible = energy.entrySet().stream().flatMap(e -> possibleMoves(e.getKey(), e.getValue()).stream()).toList();
            possible.forEach(m -> energy.merge(m.state, m.energy, (a,b) -> min(a,b))); // merge new minimum energy values
            // check if we have finish moves and get minimum energy for those
            int possinleMin = possible.stream().filter(m -> finished(m.state)).mapToInt(m -> m.energy).min().orElse(Integer.MAX_VALUE);
            min = min(min, possinleMin);
        }
        return min;
    }

    static boolean finished(State state) { // All rooms have correct entries inside
        return IntStream.range(0, 4).mapToObj(i -> state.rooms.get(i).stream().allMatch(r -> r.charAt(0) == 'A' + i)).allMatch(b -> b == true);
    }

    static List<Move> possibleMoves(State state, int energy) {
        return state.amphipods().flatMap(amph -> possibleMoves(amph, state, energy).stream()).toList();
    }

    static List<Move> possibleMoves(String amphipod, State state, int energy) { // all the possible moves for single amphipod
        List<Move> moves = new ArrayList<>();
        moves.addAll(hallwayToRoom(amphipod,  state, energy));
        moves.addAll(roomToHallway(amphipod, state, energy));
        return moves;
    }

    static List<Move> roomToHallway(String amph, State state, int energy) {
        OptionalInt currentRoom = IntStream.range(0, 4).filter(i -> state.rooms.get(i).contains(amph)).findAny();
        if (finished(amph, state) || !currentRoom.isPresent()) {
            return List.of();
        }
        List<String> room = state.rooms.get(currentRoom.getAsInt());
        if (!room.stream().dropWhile(p -> p.equals(".")).findFirst().get().equals(amph)) { // first in room to m ove
            return List.of();
        }
        int toHallwaySteps = room.indexOf(amph) + 1;
        int hallIndex = roomHallIndex(currentRoom.getAsInt());

        List<Move> res = new ArrayList<>();
        for (int i = 0; i < 11; i++) { // iterate through hall 
            if (!roomToHallway.contains(i) && clearPath(amph, i, hallIndex, state)) { // ignore doorways and check for clear path
                List<String> r = state.rooms.get(currentRoom.getAsInt());
                var nroom = replace(r, r.indexOf(amph), ".");
                var rooms = replace(state.rooms, currentRoom.getAsInt(), nroom);
                var hall = replace(state.hallway, i, amph);
                State s = new State(rooms, hall); // new state
                res.add(new Move(s, energy + energy(amph, toHallwaySteps+Math.abs(hallIndex-i))));
            }

        }
        return res;
    }

    static List<Move> hallwayToRoom(String amph, State state, int energy) {
        int amphIdx = state.hallway.indexOf(amph);
        if (amphIdx != -1) {  // amphipod is in hallway
            List<String> room = targetRoom(amph, state);
            int hallRoomIdx = targetRoomHallIndex(amph, state);
            if (acceptIntoRoom(room, amph) && clearPathToRoom(amph, state)) { // it can move to room
                int roomIdx = IntStream.range(0, room.size()).filter(i -> !room.get(i).equals(".")).findFirst().orElse(room.size())-1;
                var newRoom = replace(room, roomIdx, amph);
                var rooms = replace(state.rooms, targetRoomIndex(amph), newRoom);
                var hall = replace(state.hallway, amphIdx, ".");
                int steps = max(hallRoomIdx, amphIdx) - min(hallRoomIdx, amphIdx);
                Move m = new Move(new State(rooms, hall), energy + energy(amph, steps + roomIdx + 1));
                return List.of(m);
            }
        }
        return List.of();
    }

    static Map<Character,Integer> energy = Map.of('A',1, 'B', 10, 'C', 100, 'D', 1000);

    static int energy(String amph, int steps) {
        return energy.get(amph.charAt(0)) * steps;
    }

    static boolean acceptIntoRoom(List<String> room, String amphipod) {
        return !room.stream().anyMatch(a -> !(a.startsWith(amphipod.substring(0, 1)) || a.equals(".")));
    }

    static int targetRoomHallIndex(String amphipod, State state) {
        return roomToHallway.get(amphipod.charAt(0)-'A');
    }

    static List<Integer> roomToHallway = List.of(2, 4, 6, 8);
    static int roomHallIndex(int roomIndex) {
        return roomToHallway.get(roomIndex);
    }

    static List<String> targetRoom(String amph, State state) {
        return state.rooms.get(targetRoomIndex(amph));
    }

    static int targetRoomIndex(String amph) {
        return amph.charAt(0) - 'A';
    }

    static boolean finished(String amph, State state) {
        List<String> room = targetRoom(amph, state);
        if (!room.contains(amph)) {
            return false;
        }
        return !room.stream()
            .dropWhile(a -> !a.equals(amph))
            .anyMatch(a -> !a.startsWith(amph.substring(0,1)));
    }

    static boolean clearPath(String amph, int idx1, int idx2, State state) {
        List<String> path = state.hallway.subList(min(idx1, idx2), max(idx1, idx2)+1);
        return !path.stream().anyMatch(h -> !h.equals(amph) && h.length() > 1);
    }

    static boolean clearPathToRoom(String amph, State state) {
        return clearPath(amph, targetRoomHallIndex(amph, state), state.hallway.indexOf(amph), state);
    }

    static  boolean sameTypeInLast(char amphipod, String room) {
        return Character.toUpperCase(room.charAt(1)) == Character.toUpperCase(amphipod);
    }

    static <T> List<T> replace(List<T> list, int index, T value) {
        List<T> l = new ArrayList<>(list);
        l.set(index, value);
        return l;
    } 
}