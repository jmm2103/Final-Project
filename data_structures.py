class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

class PlayerQueue:
    def __init__(self):
        self.front = None
        self.rear = None
        self.size = 0

    def is_empty(self):
        return self.front is None

    def enqueue(self, player):
        new_node = Node(player)
        if self.rear is None:
            self.front = self.rear = new_node
        else:
            self.rear.next = new_node
            self.rear = new_node
        self.size += 1

    def dequeue(self):
        if self.is_empty():
            return None
        temp = self.front
        self.front = temp.next
        if self.front is None:
            self.rear = None
        self.size -= 1
        return temp.data

    def peek(self):
        if self.is_empty():
            return None
        return self.front.data

class MoveStack:
    def __init__(self):
        self.items = []

    def is_empty(self):
        return len(self.items) == 0

    def push(self, move):
        self.items.append(move)

    def pop(self):
        if not self.is_empty():
            return self.items.pop()
        return None

    def peek(self):
        if not self.is_empty():
            return self.items[-1]
        return None

class GamePiece:
    def __init__(self, color, start_pos):
        self.color = color
        self.position = start_pos
        self.is_home = True
        self.completed = False

class GameBoard:
    def __init__(self):
        self.board_size = 15  # 15x15 grid
        self.pieces = {}  # Dictionary to store game pieces
        self.initialize_board()

    def initialize_board(self):
        # Initialize the board with empty spaces
        self.board = [[None for _ in range(self.board_size)] for _ in range(self.board_size)]
        
    def move_piece(self, piece, steps):
        # This will be implemented with the actual movement logic
        # Returns True if move is valid, False otherwise
        pass

    def is_valid_move(self, piece, steps):
        # This will be implemented with the movement validation logic
        pass
