class Point
    def initialize(x, y)
        @x = x
        @y = y
    end

    def to_s
        return '(' + @x.to_s + ';' + @y.to_s + ') '
    end

    def get_x
        return @x
    end

    def get_y
        return @y
    end
end

def calculate_square(points)
    if (points.is_a?(Array) == false)
        puts "Error: you should enter array of points"
        return
    end

    if (points.length == 0)
        puts "Error: you should pass not empty array"
        return
    end

    sum = 0
    length = points.length
    for i in 0..(length - 2)
        sum = sum + (points[i].get_x + points[i + 1].get_x)*(points[i].get_y - points[i + 1].get_y)
    end
    sum = sum + (points[length - 1].get_x + points[0].get_x)*(points[length - 1].get_y - points[0].get_y)
    sum = sum.abs
    return sum / 2.0

end


def main
    puts "Shape 1:"
    points_1 = [Point.new(150,49), Point.new(221,78), Point.new(219,121), Point.new(208,158), Point.new(167,182)]
    puts points_1
    puts "Square = ", calculate_square(points_1), "\n\n"

    puts "Shape 2:"
    points_2 = [Point.new(108,192), Point.new(72,171), Point.new(59,133), Point.new(76,115), Point.new(91,97)]
    puts points_2
    puts "Square = ", calculate_square(points_2), "\n\n"

    puts "Shape 3:"
    points_3 = [Point.new(75,78), Point.new(67,58), Point.new(79,47), Point.new(98,30), Point.new(118,22)]
    puts points_3
    puts "Square = ", calculate_square(points_3), "\n\n"

    puts "Shape 4:"
    points_4 = [Point.new(136,30), Point.new(143,37)]
    puts points_4
    puts "Square = ", calculate_square(points_4), "\n\n"

end


main