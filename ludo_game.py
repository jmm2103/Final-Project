import pygame
import sys
from data_structures import PlayerQueue, MoveStack, GameBoard
import random

# Initialize Pygame
pygame.init()

# Constants
WINDOW_SIZE = 800
SCREEN = pygame.display.set_mode((WINDOW_SIZE, WINDOW_SIZE))
pygame.display.set_caption("Ludo Game")

# Colors
WHITE = (255, 255, 255)
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (0, 0, 255)
YELLOW = (255, 255, 0)
BLACK = (0, 0, 0)

class LudoGame:
    def __init__(self):
        self.board = GameBoard()
        self.player_queue = PlayerQueue()
        self.move_stack = MoveStack()
        self.current_player = None
        self.dice_value = 1
        
    def initialize_game(self):
        # Add players to queue
        colors = [RED, GREEN, BLUE, YELLOW]
        for color in colors:
            self.player_queue.enqueue(color)
        self.current_player = self.player_queue.peek()

    def roll_dice(self):
        self.dice_value = random.randint(1, 6)
        return self.dice_value

    def draw_board(self):
        SCREEN.fill(WHITE)
        # Draw the basic board structure
        # This will be expanded in the next iteration
        pygame.draw.rect(SCREEN, BLACK, (50, 50, WINDOW_SIZE-100, WINDOW_SIZE-100), 2)

    def run(self):
        self.initialize_game()
        running = True
        while running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                elif event.type == pygame.MOUSEBUTTONDOWN:
                    # Handle mouse clicks
                    pass

            self.draw_board()
            pygame.display.flip()

        pygame.quit()
        sys.exit()

if __name__ == "__main__":
    game = LudoGame()
    game.run()
