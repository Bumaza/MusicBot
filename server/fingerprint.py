import math, wave
from MusicBot.utils import hash, get_index, RANGE
from MusicBot.fft import fft


class FingerPrint:

    def __init__(self, path, handler):
        with wave.open(path) as fd:
            self.__params = fd.getparams()
            self.__song_bytes = fd.readframes(self.__params.nframes)
        self.__handler = handler
        self.__points = 4096
        self.__name = str(path).split('/')[-1]

    def create_fingerprint(self):
        print('Processing...')

        result = [fft([complex(self.__song_bytes[(i * self.__points)+j], 0)
                   for j in range(self.__points)])
                  for i in range(len(self.__song_bytes) // self.__points)]

        print('     HASH               ', '\t'.join(str(r)+'Hz' for r in RANGE))
        for t in range(len(result)):
            high_scores, points = [0 for _ in range(len(RANGE))], [0 for _ in range(len(RANGE))]
            for freq in range(RANGE[0], RANGE[-1]):
                mag = math.log(abs(result[t][freq]) + 1)
                index = get_index(freq)
                if mag > high_scores[index]:
                    high_scores[index] = mag
                    points[index] = freq
            h = hash(points)
            print(h, '\t\t', '\t\t'.join(str(p) for p in points))
            self.__handler.add_finger_print(h, t, self.__name)