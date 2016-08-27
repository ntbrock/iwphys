class ProblemController < ApplicationController
  layout 'default'


  def browse
    @path = params[:path]
    @path = '/' if ! @path

    @subdirs = []

    @files = []


  end
end
