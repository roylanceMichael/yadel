from setuptools import setup, find_packages
import properties

actual_name = properties.group + "_" + properties.name
actual_version = properties.major + "." + properties.minor
setup(
    name=actual_name,
    version=actual_version,
    author=properties.author,
    license=properties.license,
    include_package_data=True,
    install_requires=['protobuf==3.0.0'],
    description=properties.description,
    author_email=properties.author,
    url=properties.githubUrl,
    packages=find_packages(exclude=['tests']))