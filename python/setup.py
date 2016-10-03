from setuptools import setup, find_packages

setup(
    name="org_roylance_yadel_api",
    version="0.117",
    author="roylance.michael@gmail.com",
    license='MIT',
    include_package_data=True,
    install_requires=['protobuf==3.0.0'],
    description="models to interface with the org.roylance.yadel.api system",
    author_email="roylance.michael@gmail.com",
    url="https://github.com/roylanceMichael/yadel.git",
    packages=find_packages(exclude=['tests']))