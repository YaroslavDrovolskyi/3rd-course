def func_y(x, n, c)
    result = (x**(1.to_f/4) - (1.to_f/x)**(1.to_f/4))**((-1)*n) + 
        (x + 1).to_f/(x**2 - 4*x + 3*n) + ((36*x*c*n)**(1.to_f/4))/((x + c*n + 1).to_f/(x+5))

    return result
end

def func_z(x, n, c)
    result = ((Math.tan(2*x)) * (Math.cos(2*x))**(-1) - (Math.tan(2*c + x)) * (Math.cos(2*x))**(-2)).to_f/((Math.cos(2*x))**(-1) + (Math.cos(3*x))**(-2)) +
        (1 + (Math.cos(n*x))**(1.to_f/c))/(2*x + (Math.sin(3*x))**2)

    return result
end

def func_f(x, n, c)
    if (x > 2 && x < n)
        return func_y(x, n, c)
    elsif (x > n && x < 2*n)
        return func_z(x, n, c)
    else
        return func_y(x, n, c) + func_z(x, n, c)
    end
end