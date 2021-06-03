import pickle

class example_class:
    a_number = 35
    a_string = "hey"
    a_list = [1, 2, 3]
    a_dict = {"first": "a", "second": 2, "third": [1, 2, 3]}
    a_tuple = (22, 23)

def predict(n, p, k, ):
    my_object = example_class()
    my_pickled_object = pickle.dumps(my_object)  # Pickling the object
    return f"{n}:\n{my_pickled_object}\n"
